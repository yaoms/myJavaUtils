package conch.yaoms.test.bot;


public class DefaultTracker implements ITracker {

	private String tag;

	public DefaultTracker(String tag) {
		setTag(tag);
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	@Override
	public void info(Object object) {
		printLog(2, tag, object);
	}

	@Override
	public void warn(Object object) {
		printLog(1, tag, object);
	}

	@Override
	public void error(Object object) {
		printLog(0, tag, object.toString());
	}

	@Override
	public void error(Object object, Throwable tr) {
		printLog(0, tag, object.toString() + tr.getMessage());
	}

	private void printLog(int level, Object tag, Object msg) {
		String now = String.format("%1$tH:%1$tM:%1$tS",
				System.currentTimeMillis());
		String lvString = "INFO";
		switch (level) {
		case 0:
			lvString = "ERROR";
			break;
		case 1:
			lvString = "WARN";
			break;
		case 2:
			lvString = "INFO";
			break;
		}
		System.out.println(String.format("[%s] [%-5s] [%s] %s", now, lvString,
				tag, msg));
	}

}
