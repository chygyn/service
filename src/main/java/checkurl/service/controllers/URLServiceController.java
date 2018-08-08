package checkurl.service.controllers;

import checkurl.service.bl.CheckURLThread;
import checkurl.service.entity.Config;
import checkurl.service.entity.Texts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import checkurl.service.bl.Util;

import java.util.Map;


@RestController
@RequestMapping(value = "/service")
public class URLServiceController {

    CheckURLThread r;

    @RequestMapping(method = RequestMethod.POST, value = "/setConfig")
    public ResponseEntity<String> postServiceConfig(@RequestBody Config conf) {
        System.out.println(Texts.post_setConfig+conf.toString());
        if (Util.checkConfig(conf)) {
            conf.toString();
            Map<Thread, StackTraceElement[]> threads = Thread.getAllStackTraces();
            if (r!=null)
                r.interrupt();
            r = new CheckURLThread(conf);
            return new ResponseEntity<String>(Texts.config_correct,HttpStatus.OK);
        } else {
            return new ResponseEntity<String>(Texts.config_wrong,HttpStatus.OK);
        }
    }

    @RequestMapping(method = RequestMethod.GET, value = "/getURLStatus")
    public ResponseEntity getURLStatus() {
        if (r==null){
            return new ResponseEntity<String> (Texts.no_config, HttpStatus.BAD_REQUEST);
        }
        Map <String,String> urlStatus = r.getThreadValues();
        System.out.println(Texts.get_getURLStatus+urlStatus);
        return new ResponseEntity<Map <String,String>>(urlStatus, HttpStatus.OK);
    }
}
