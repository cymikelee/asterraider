// Pref class
// by Mike Lee

public class Pref {
	// Fields
	private String name;
	private int value;

	// Constructors
	public Pref(String n, int v) {
		name = n;
		value = v;
	}

	public Pref(String prefString) {
		name = new String();
		int i = 0;
		do {
			name += prefString.charAt(i);
			i++;
		} while(prefString.charAt(i) != '=');
		i++;
		String temp = new String();
		while(i < prefString.length()) {
			temp += prefString.charAt(i);
			i++;
		}
		value = Integer.parseInt(temp);
	}

	public Pref(Pref p) {
		name = p.name;
		value = p.value;
	}

	// Methods
	public void setName(String n) {
		name = n;
	}

	public void setValue(int v) {
		value = v;
	}

	public String getName() {
		return name;
	}

	public int getValue() {
		return value;
	}

	public String toString() {
		return (name+'='+Integer.toString(value));
	}

}

