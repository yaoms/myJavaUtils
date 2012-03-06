package conch.yaoms.connection;

public class DWQueue {
	
	static public final byte MAXNUM = 5;

	int front; // 头位置

	int rear; // 尾位置

	private Object[] objects;

	public DWQueue() {
		objects = new Object[MAXNUM];
		front = 0;
		rear = 0;
	}

	public void push(Object value) {
		if (value == null) {
			return;
		}

		if ((rear + 1) % MAXNUM != front) {
			objects[rear] = value;
			rear = (rear + 1) % MAXNUM;
		}
	}

	public Object pop() {
		Object obj = null;
		if (front != rear) {
			obj = this.objects[this.front];
			this.front = (this.front + 1) % MAXNUM;
		}
		return obj;
	}

	public boolean isFull() {
		if ((rear + 1) % MAXNUM == front) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEmpty() {
		if (rear == front) {
			return true;
		} else {
			return false;
		}
	}
}
