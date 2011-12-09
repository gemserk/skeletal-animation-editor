package com.gemserk.commons.tasks;


public class TaskDelayedImpl implements Task {

	Task task;
	float delay;

	public TaskDelayedImpl(Task task, float delay) {
		this.task = task;
		this.delay = delay;
	}

	@Override
	public boolean isDone() {
		return task.isDone();
	}

	@Override
	public void update(float delta) {
		if (delay < 0)
			// should call with delta - delay?
			task.update(delta);
		else
			delay -= delta;
	}

}