# post-office-agent-rd-automation
Repo helps RD agent for automating their payment list on portal

To setup the project, Please the below steps

Pre-requisites:
Windows 7, 8 10, 11

1) Install GitBash Application of latest version into your system.
2) Connect your local with github remote using ssh key.
3) (Optional) Deploy bashrc for the ease.
4) Download and Install Java to the system with the version 1.8 or above.
   And make sure environmental variables of jdk folder are set.
5) Download and Install gradle to the users folder.

After doing all above 5 steps, We have completed initial setup here.

Then, Clone this repository into your local folder of my name (Pranav) followed by PostOffice
i.e. (Pranav/Postoffice).

Now, Set the username and password into the respective files which is having path 
"src/test/resources/test_data/". And also add required account numbers separated by ",".

Now Try to run command "./gradlew cucumber -P tags=@Create" OR As per Step 3 Setup the bashrc and put alias
whatever you like.

Thank you!!