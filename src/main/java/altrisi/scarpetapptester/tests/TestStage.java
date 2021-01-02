package altrisi.scarpetapptester.tests;

public enum TestStage {
	WAITING,
	ASYNC_PREPARING,
	SYNC_PREPARING,
	RUNNING,
	ASYNC_CHECKING,
	SYNC_CHECKING,
	FINISHING,
	FINISHED;
}
