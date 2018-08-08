package checkurl.service.bl;

import checkurl.service.entity.Config;
import checkurl.service.entity.Texts;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static checkurl.service.bl.Util.checkURL;

public class CheckURLThread implements Runnable {
    private Config conf;
    private Map<String, String> urlMap;
    private Map<String, String> lastCheck;
    private Thread t;
    private boolean runnable;

    public CheckURLThread(Config conf) {
        t = new Thread(this, "checkURLThread");
        this.conf = conf;
        this.urlMap = new HashMap<String, String>();
        this.lastCheck = new HashMap<String, String>();
        this.runnable = false;
        t.start();
    }

    @Override
    public void run() {
        System.out.println(Texts.run_CheckUrlThread + this.conf.toString());
        List<String> urls = this.conf.getURL();
        if (conf.getMode() <= 0) {
            startCheckUrl(urls);
        } else {
            int threadCount = (int) conf.getMode();
            //define size of urls pack in one thread.
            int sizePack = (int) Math.ceil((double) urls.size() / (double) threadCount);
            startCheckUrl(urls, threadCount, sizePack);
        }
    }

    class CheckPackThread implements Callable {
        private List<String> listUrl;
        public CheckPackThread(List<String> listUrl) {
            this.listUrl = listUrl;
        }
        public Map<String, String> call() {
            Map<String, String> packResult = checkListUrl(listUrl);
            return packResult;
        }
    }


    public synchronized Map<String, String> getThreadValues() {
        return this.lastCheck.size()==0 ? this.urlMap:this.lastCheck;
    }
    //stop sleep by changing flag and after interrupt thread
    public synchronized void interrupt() {
        this.runnable = false;
        t.interrupt();
    }

    private void startCheckUrl(List<String> urls, int threadCount, int sizePack) {
        long delay = conf.getTimer();
        this.runnable = true;
        while (this.runnable) {
            //define array of packs. their count equals to number of threads defined in mode
            System.out.println(Texts.timer_many_threads+urls+" timer:"+delay+" msec, threads: "+threadCount);
            ArrayList<List<String>> pack = new ArrayList<List<String>>();
            int curUrl = 0;
            for (int i = 0; i < threadCount; i++) {
                List<String> p = new ArrayList<String>();
                //fill pack by number of urls equals to sizepack
                if (curUrl>=urls.size())
                    break;
                for (int j = curUrl; j < curUrl + sizePack; j++) {
                    p.add(urls.get(j));
                    if (j + 1 >= urls.size())
                        break;
                }
                curUrl += sizePack;
                pack.add(p);
            }
            ExecutorService checkPackService = Executors.newFixedThreadPool(threadCount);
            Set<Future<Map<String, String>>> set = new HashSet<Future<Map<String, String>>>();
            for (int t = 0; t < pack.size(); t++) {
                Callable<Map<String, String>> packCheck = new CheckPackThread(pack.get(t));
                Future<Map<String, String>> res = checkPackService.submit(packCheck);
                set.add(res);
            }
            this.urlMap.clear();
            for (Future<Map<String, String>> future : set) {
                try {
                    Map<String, String> mapRes = future.get();
                    for (Map.Entry<String, String> entry : mapRes.entrySet()) {
                        this.urlMap.put(entry.getKey(), entry.getValue());
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
            checkPackService.shutdown();
            this.lastCheck.clear();
            this.lastCheck.putAll(this.urlMap);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private void startCheckUrl(List<String> urls) {
        long delay = conf.getTimer();
        this.runnable = true;
        while (this.runnable) {
            System.out.println(Texts.timer_no_threads+urls+" timer:"+delay+" msec");
            this.urlMap.clear();
            this.urlMap.putAll(checkListUrl(urls));
            this.lastCheck.clear();
            this.lastCheck.putAll(this.urlMap);
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    private Map<String, String> checkListUrl(List<String> urls) {
        Map<String, String> urlMap = new HashMap<String, String>();
        for (String url : urls) {
            String status = "";
            try {
                status = checkURL(url);
            } catch (IOException e) {
                status = Texts.error_check_url;
            }
            urlMap.put(url, status);
        }
        return urlMap;
    }

}
