package org.net.Net.MQTT;


import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Production {
    public static void main(String[] args) {
            String subTopic = "testTopic/#";
            String pubTopic = "testTopic/1";

            String content = "2201230481902381023";
            int qos = 1;

            String broker = "tcp://127.0.0.1:1883";
            String clientId = "Java_Pub";
            MemoryPersistence persistence = new MemoryPersistence();

            try{

                MqttClient client = new MqttClient(broker, clientId, persistence);
                // MQTT 连接选项
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setUserName("admin");
                connOpts.setPassword("public".toCharArray());
                connOpts.setCleanSession(true);
                connOpts.setKeepAliveInterval(30);
                client.setCallback(new MessageCallBack());

                client.connect();

                System.out.println("Connected to broker: "+broker);

//                client.subscribe(subTopic);

                MqttMessage mqttMessage = new MqttMessage(content.getBytes());
                mqttMessage.setQos(qos);
                for(;;){
                    client.publish(pubTopic, mqttMessage);
                    System.out.println("Message published");
                    Thread.sleep(4000);
                }

//                Thread.sleep(10000);

//                client.disconnect();
//                System.out.println("Disconnected");
//                client.close();
//                System.exit(0);

            }catch (MqttException me){
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

    }
}
