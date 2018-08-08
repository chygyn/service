# service

Service for checking urls. 
This service contain two servlet:
- post /setConfig for sending configuration file. File format: {url:["http://.."],timer:int, mode:long}, where url - array of urls in string format,
timer - timer in mseconds for repeat checking of urls, mode - mode of service working. Mode<=0 then service works with one thread, 
mode>=0 then service works with number of threads equals mode. 
- get /getURLStatus for getting result of last url checking. It returns map<String,String> where key = url, value = status of last checking.

