package com.gemserk.commons.tasks;


public abstract class TaskImpl implements Task {

	boolean done = false;

	@Override
	public boolean isDone() {
		return done;
	}
	
	public void setDone(boolean done) {
		this.done = done;
	}

}