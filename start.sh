mvn clean package -Dmaven.test.skip=true
nohup java -jar dns_web_control/target/dns-web-control-1.0-SNAPSHOT.jar >/dev/null &