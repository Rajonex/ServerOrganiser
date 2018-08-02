package sends;

public class Ack {
	String message;
	boolean confirm;

	public Ack() {
	}

	public Ack(String message, boolean confirm) {
		super();
		this.message = message;
		this.confirm = confirm;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isConfirm() {
		return confirm;
	}

	public void setConfirm(boolean confirm) {
		this.confirm = confirm;
	}

	@Override
	public String toString() {
		return "Ack [message=" + message + ", confirm=" + confirm + "]";
	}

}
