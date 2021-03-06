# ScarpetAppTester Scarpet Docs
ScarpetAppTester supports creating, managing and checking tests directly from a custom Scarpet app.

In order to do so, it provides a few events and methods so it can be interfaced directly from Scarpet.

## Events

Events allow Scarpet tester apps to complement the code during the execution of the tests.

Note that none of them will run if the test was generated directly from Scarpet, since those already accept callbacks
when those events occur.

### `__on_preparing_tests(appName)`

Triggers when tests for an app that is going to be tested are being prepared.

This is the moment to add custom tests.

### `__on_pre_test_started(appName, test)`

Should be used in order to prepare a test before it runs.

Runs before the test starts, waiting for schedules or anything prepared to finish before starting the test.

Gets you an instance of the test in order to add results or set it as failed.

### `__on_right_before_test_started(appName, test)` 

Runs _right before_ test starts. Once the code in this event is ran, no matter if there were schedules or not, the test
will start immediately.

Schedules from this event, the test itself, or `__on_right_after_test_started` will be processed before concluding the test.

Gets you an instance of the test in order to add results or set it as failed.

### `__on_right_after_test_started(appName, test)`

Runs _right after_ the test starts, without waiting for any schedules from it or `__on_right_before_test_started`.

Schedules from this event, the test itself, or `__on_right_before_test_started` will be processed before concluding the test.

Gets you an instance of the test in order to add results or set it as failed.

### `__on_test_finished(appName, test, successfulSoFar)`

Runs after the test has finished and preliminary checks by ScarpetAppTester have been executed. `successfulSoFar` is a boolean of the results from
those tests executed by ScarpetAppTester.

The app can compute and send new results that will be added to the report in this event.

Gets you an instance of the test in order to add results or set it as failed.

## API

Those functions are useful to manage or even create tests directly from Scarpet.

### TODO: Test subject creator

### `create_test(name, testSubject, prepareFunction, rightBeforeFunction, rightAfterFunction, checkingFunction)`

Creates (and returns) a new test for the current app being tested.

Test subject must have been generated using [not yet existing function], and the rest of the functions must be either function names directly,
without arguments, or a lambda including a function to run in those.

Tests generated by this function will not run the test phase events, since those can be directly handled by the functions present in the arguments, or
null to not run anything for that phase.
```py
__on_preparing_tests(appName) -> (
	if (appName != 'myapp', return());
	create_test('Check blocks are placed correctly', testSubject, 'prepare_check_blocks', null, null, _() -> (
		incorrect_positions = [];
	    volume([0,0,0],[5,5,5],
    		if(_ != 'white_concrete',
	    		put(incorrect_positions,null,pos(_));
    		);
    		if(incorrect_positions,
    			add_result('failure', 'Some positions weren\'t filled correctly'+incorrect_positions); //FIXME new syntax
	    	,
    			add_result('pass', 'All blocks were filled correctly');
    		);
	    )
	));
);
```

### `add_result(test, resultName, result)`

Adds a result to the specified test.

Result name should be the name of what was being checked in that stage, and result should be, well, the result of such stage.

For example, for the loading test, the equivalent in Scarpet would be:

```py
add_result(loadingTest, 'App load', 'Correct');
add_result(loadingTest, 'Elapsed Time', '24ms');
```

### `set_failed(test)`

Sets a test as failed.
