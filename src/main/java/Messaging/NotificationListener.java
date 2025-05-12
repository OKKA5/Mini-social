package Messaging;

import jakarta.ejb.ActivationConfigProperty;
import jakarta.ejb.MessageDriven;
import jakarta.jms.*;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/notificationsTopic"),
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "java:jboss/jms/queue/Mini-SocialQueue")// or Topic, if it's actually a topic
})
public class NotificationListener implements MessageListener {

    private final static Logger LOGGER = Logger.getLogger(NotificationListener.class.getName());

    @Override
    public void onMessage(Message rcvMessage) {
        try {
            if (rcvMessage instanceof TextMessage) {
                TextMessage msg = (TextMessage) rcvMessage;
                LOGGER.info("Received Message from notificationsTopic ===> " + msg.getText());
            } else {
                LOGGER.warning("Incorrect Message Type!");
            }
        } catch (JMSException e) {
            throw new RuntimeException("Error processing message", e);
        }
    }
}