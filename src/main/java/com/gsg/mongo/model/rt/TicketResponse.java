package com.gsg.mongo.model.rt;

public class TicketResponse {
	private String id;
	private String _url;
	private String type;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String get_url() {
		return _url;
	}

	public void set_url(String _url) {
		this._url = _url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TicketResponse [id=" + id + ", _url=" + _url + ", type=" + type + "]";
	}

}
