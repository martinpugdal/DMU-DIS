//opg7pgm

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class SimpleMqttCallBack implements MqttCallback {
    private int status = 0;
    private double humidityMax = 32;

    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String res = new String(mqttMessage.getPayload());
        JSONObject jsonObject = new JSONObject(res);
        System.out.println(jsonObject);

        JSONObject object = jsonObject.getJSONObject("AM2301");
        double humid = object.getDouble("Humidity");

        boolean statusChanged = false;
        if (humid > humidityMax && status == 0) {
            statusChanged = true;
        }
        else if (humid <= humidityMax && status == 1) {
            statusChanged = true;
        }

        // if status is changed, when we want to switch the power
        if (statusChanged) {
            status = status == 0 ? 1 : 0; // switch to 1 or 0
            MQTTprogram.publishMessage(
                    MQTTprogram.sampleClient,
                    "cmnd/grp3336/Power1",
                    String.valueOf(status)
            );
        }
    }

    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        // not used in this example
    }
}
