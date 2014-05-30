bachelor-wars
=============

bachelor-wars - project in JASON

To run application:

1. Download Eclipse for Java developers -> http://www.eclipse.org/downloads/ (tested on versions Kepler SR1 and SR2)

2. Install plugin for Jason from http://jason.sourceforge.net/mini-tutorial/eclipse-plugin/. Used plugin version : 1.0.10 Steps about installation of Jason can be ignored, if you want to follow these steps be sure you are using jason 1.4.0a or above)

3. In Eclipse switch into Jason perspective

4. clone actual branch "new _step" into eclipse

5. If you haven't installed Jason, go into folder which contains cloned repository and in "lib/" run command:
  "java -jar jason.jar"
  
6. There should be all filled automatically -> if not, the important ones are only jason.jar location and ant libs location:
  jason.jar location -> "path_to_project/lib/jason.jar"
  ant libs location -> "path_to_project/lib"
  
7. Restart Eclipse (necessary)

8. Run Jason project - be sure you are in Jason perspective
