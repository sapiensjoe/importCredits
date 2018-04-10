package mx.wiri.giro.batch.dao;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;

public interface ICallBatchDAO
{
	public String preHandler(Integer branchid, Integer usertype, String device, String browser, String ip,
							 Timestamp date, Integer transactionid, String request, Integer staffid) throws IOException;

	public String getSessionBdByCallId(String call_id) throws IOException;

	public String importCreditsBatch(String sessionBd, String callId, Integer productId) throws IOException;

	public String afterComplete(String call_id, String responseType, String responseDetail, Timestamp date) throws IOException;
}




