package io.protostuff;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
public class FooValue {

	@XmlAttribute
	private int id;
	
	@XmlAttribute
	private String name;

	@XmlValue
	private long value;

	public FooValue() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("FooValue [");
		sb.append(" id: ").append(id).append(", name: ").append(name).append(", value: ").append(value).append(" ]");
		return sb.toString();
	}
}
