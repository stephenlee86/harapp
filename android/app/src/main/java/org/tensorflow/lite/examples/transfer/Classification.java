package org.tensorflow.lite.examples.transfer;

/*
handles the data associated with each label or classification
holds the name and instance count
*/

public class Classification {
	String name;
	int instance_count;

	public Classification (String n, int c) {
		this.name = n;
		this.instance_count = c;
	}

	public String getName () {
		return this.name;
	}

	public int getInstanceCount () {
		return this.instance_count;
	}

	// method to increment instance count, will increment by 1 by overloading, but an alternative int can be passed to increment to increment by more than 1

	public void incrementInstanceCount (int inc) {
		this.instance_count += inc;
		return;
	}

	public void incrementInstanceCount () {
		this.incrementInstanceCount (1);
		return;
	}
}