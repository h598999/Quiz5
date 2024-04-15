** Commands **

./gradlew task-name
Builds the application

adb install path/to/your_app.apk
This command is used to install the application on the started emulated android device

adb shell am instrument -w com.example.quiz5/androidx.test.runner.AndroidJUnitRunner

We the run the adb shell am instrument with the -w flag. The -w flag forces am instrument to wait til the instrumentations are finished before it terminates itself
This keeps the shell open until the tests are finished and are required to get the output from the tests

Then we enter the packagename of the tests then a / and then we enter the Runner class of the test which is often androidx.test.runner.AndroidJunitRunner

