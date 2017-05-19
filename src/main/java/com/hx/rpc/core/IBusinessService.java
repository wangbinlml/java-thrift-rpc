package com.hx.rpc.core;

import com.hx.rpc.gen.Msg;

public interface IBusinessService {
	public Msg doBusiness(String messageName, Msg reqMsg);
}
