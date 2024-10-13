# Console to File Action

## Technology
- Language: Java 17
- Build tool: gradle


## About
This is small plugin provides opportunity to save your console output data as 
plain text file on your local machine by choosing any available directory.
Name of saved file is '*console_output*' with current timestamp date formatted 
as yyyy.MM.dd.HH_mm.


## How To
To try out this plugin you should start up any Intellij Idea instance on your 
local machine and *Run* any project you like. After that go to your Run-console and
press right mouse key in console-window. If you have any text in idea console 
instance the **Export to file** will be available to press. In opened window
choose any directory on your local machine and press *Ok*. After those actions 
a file with name *console_output.yyyy.MM.dd.HH_mm* will be created at the directory
which you specified.