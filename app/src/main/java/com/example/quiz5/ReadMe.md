** Commands **

./gradlew task-name
Builds the application

adb install path/to/your_app.apk
This command is used to install the application on the started emulated android device

adb shell am instrument -w com.example.quiz5/androidx.test.runner.AndroidJUnitRunner
We the run the adb shell am instrument with the -w flag. The -w flag makes the run wait 
for a debugger to be connected 

Then we enter the packagename of the tests

