package Messaging;

import jakarta.annotation.Resource;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import jakarta.jms.*;

@Startup
@Singleton
public class JMSClient {

    //TODO Map the destination queue to the admin object using JNDI and @Resource
    @Resource(mappedName = "java:jboss/jms/queue/Mini-SocialQueue")
    private Queue MiniSocialQueue;

    //TODO Inject a JMSContext to get a Connection and Session to the embedded broker
    @Inject
    JMSContext context;

    public void sendMessage(NotificationEvent msg) {
        try {
            //TODO Create a JMSProducer
            JMSProducer producer = context.createProducer();

            //TODO Create a TextMessage
            String json = msg.toString();
            TextMessage message = context.createTextMessage(json);

            //TODO Send the message
            producer.send(MiniSocialQueue, message);

            System.out.println("Sent Message: "+ msg);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}