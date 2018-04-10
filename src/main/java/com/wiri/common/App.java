package com.wiri.common;

import mx.wiri.giro.batch.dao.impl.CallBatchDAOImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class App 
{
    public static void main( String[] args ) throws IOException {
    	ApplicationContext context = new ClassPathXmlApplicationContext("Spring-Module.xml");
    	 
        CallBatchDAOImpl callBatchDAO = (CallBatchDAOImpl) context.getBean("CallBatchDAO");
        String[] resp={};
        String codRes="";
        String response="";

        //header
        System.out.println("\n\n******************************************   IMPORT CREDITOS   ******************************************\n");

        //prehandler
        System.out.println("\n\n------------------------------------------------preHandler-----------------------------------------------\n");
        java.util.Date fecha_sistema = new java.util.Date();
        java.sql.Timestamp fecha_inicio = new java.sql.Timestamp(fecha_sistema.getTime());
        String callId = callBatchDAO.preHandler(3,2,"","","",fecha_inicio,6,"",1);
        System.out.println("Call ID\t\t\t\t::: " + callId);

        String sessionId = callBatchDAO.getSessionBdByCallId(callId);
        System.out.println("Session ID\t\t\t::: " + sessionId);

        //core
        System.out.println("\n\n---------------------------------------------------core--------------------------------------------------\n");
        try {

            response = callBatchDAO.importCreditsBatch(sessionId, callId, 1);
            System.out.println("Response\t\t\t::: " + response);

            resp = response.split("\\|");
            codRes = resp[0];
        }catch (Exception ex){
            System.out.println(ex);
            codRes = "failed";
            response = ex.getMessage();
        }finally {
            //afterComplete
            System.out.println("\n\n----------------------------------------------afterComplete----------------------------------------------\n");
            java.util.Date fin = new java.util.Date();
            java.sql.Timestamp fecha_fin = new java.sql.Timestamp(fin.getTime());
            String resultado = callBatchDAO.afterComplete(callId,codRes,response,fecha_fin);
            System.out.println("Response\t\t\t::: " + resultado);
            if (codRes.equalsIgnoreCase("failed")){
                System.out.println("RollBack\t\t\t::: not necessary");
                System.exit(1);
            }
        }
    }
}