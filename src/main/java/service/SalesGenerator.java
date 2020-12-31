package service;

import model.Venda;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import serializable.SalesSerializable;

import java.math.BigDecimal;
import java.util.Properties;
import java.util.Random;

public class SalesGenerator {

    private static Random random = new Random();
    private static long operation = 0;
    private static BigDecimal ticketValue = BigDecimal.valueOf(500);

    public static void main(String[] args) throws InterruptedException {
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SalesSerializable.class.getName());

        try(KafkaProducer<String, Venda> producer = new KafkaProducer<String, Venda>(properties)){
            while(true){
                Venda venda = salesGenerator();
                ProducerRecord<String, Venda> record =
                        new ProducerRecord<String, Venda>("venda_ingressos", venda);
                producer.send(record);
                Thread.sleep(200);
            }
        }



    }

    private static Venda salesGenerator() {
        long client = random.nextLong();
        int ticketsQtd = random.nextInt(10);
        return new Venda(operation++, client, ticketsQtd,
                ticketValue.multiply(BigDecimal.valueOf(ticketsQtd)));
    }
}
