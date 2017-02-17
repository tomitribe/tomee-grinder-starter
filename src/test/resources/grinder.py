# Hello World
#
# A minimal script that tests The Grinder logging facility.
#
# This script shows the recommended style for scripts, with a
# TestRunner class. The script is executed just once by each worker
# process and defines the TestRunner class. The Grinder creates an
# instance of TestRunner for each worker thread, and repeatedly calls
# the instance for each run of that thread.

from net.grinder.script.Grinder import grinder
from net.grinder.script import Test
from org.superbiz import ColorServicePerf

# A shorter alias for the grinder.logger.output() method.
log = grinder.logger.output

tests = {
    "postGreen" : Test(1, "postGreen"),
    "getColorObject" : Test(2, "getColorObject"),
    "getGreen" : Test(3, "getGreen"),
    }

loadBean = ColorServicePerf("http://localhost:8080/tomee-grinder-starter-1.0-SNAPSHOT/")
postGreen = tests["postGreen"].wrap(loadBean)
getGreen = tests["getGreen"].wrap(loadBean)
getColorObject = tests["getColorObject"].wrap(loadBean)

# A TestRunner instance is created for each thread. It can be used to
# store thread-specific data.
class TestRunner:

    # This method is called for every run.
    def __call__(self):
        postGreen.postGreen()
        getGreen.getGreen()
        getColorObject.getColorObject()
