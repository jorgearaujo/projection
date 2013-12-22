package io.projection.domain;

/**
 * Defines the relation between two fields of different classes. If the relation
 * is direct, then it is defined by two simple Strings. If the relation has
 * objects involved, then a dot is used to specify how to access to the
 * applicable instance.
 * 
 * @author Jorge Araújo
 * 
 */
public class Partnership {
	String origin;
	String target;

	public Partnership(String originValue, String targetValue) {
		this.origin = originValue;
		this.target = targetValue;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Override
	public String toString() {
		return "Partnership [origin=" + origin + ", target=" + target + "]";
	}

}
