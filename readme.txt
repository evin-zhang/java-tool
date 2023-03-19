how to create java proj by manual
create the directory list
'''
bin
  xxxxx.class
lib
  activation.jar
  javax.mail.jar
src 
   com
     kz
      DiskMonitor.java
'''

how to compile jar
javac -d bin -cp lib/*:. src/com/kz/*.java
touch Manifest.txt ,specify main class and lib 
'''
Class-Path: lib/activation-1.1.1.jar lib/javax.mail-1.6.2.jar
Main-Class: com.kz.DiskMonitor

'''
how to package jar
jar cfm diskmonitorapp.jar Manifest.txt -C bin . -C lib .

how to check jar directory list 
jar -tf xxxx.jar

how to run jar 

java -jar diskmonitorapp.jar