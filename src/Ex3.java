import java.util.ArrayList;
import java.util.List;

class Order {
    private final int visitorNum;

    public Order(int visitorNum) {
        this.visitorNum = visitorNum;
    }

    public int getVisitorNum() {
        return visitorNum;
    }
}

class Rest {
    private final int COOCKING_TIME = 3000;
    private final int MAX_VISITORS_COUNT = 5;
    private final int VISITOR_INTERVAL = 5000;
    private final int DECISION_TIME = 3000;
    private final int EAT_TIME = 3000;

    private List<Order> coockingList = new ArrayList<>(10);
    private List<Order> orderList = new ArrayList<>(10);
    private List<Order> deliveryList = new ArrayList<>(10);
    private int visitorCnt = 0;

    public synchronized void visitorAddOrder() {
        try {
            //while (true) {
            for (int i=0; i< MAX_VISITORS_COUNT; i++) {
                Thread.sleep(VISITOR_INTERVAL);
                //if (visitorCnt < MAX_VISITORS_COUNT) {
                    //++visitorCnt;
                    System.out.printf("Посетитель %s в ресторане\n", i+1);
                    Thread.sleep(DECISION_TIME);
                    //System.out.printf("Посетитель %s сделал заказ", visitorCnt);
                    orderList.add(new Order(i+1));
                    notifyAll();
                //}
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void serviceOrders() {
        System.out.println(Thread.currentThread().getName() + " на работе!");
        try {
            while (true) {
                while (orderList.isEmpty()) {
                    wait();
                }
                System.out.println(Thread.currentThread().getName() + " взял заказ");
                coockingList.add(orderList.remove(0));
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void coockingOrder() {
        System.out.println(Thread.currentThread().getName() + " на работе!");
        try {
            while (true) {
                while (coockingList.isEmpty()) {
                    wait();
                }
                System.out.println("Повар готовит блюдо");
                Thread.sleep(COOCKING_TIME);
                System.out.println("Повар приготовил блюдо");
                deliveryList.add(coockingList.remove(0));
                notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void deliveryOrder() {
        try {
            while (true) {
                while (deliveryList.isEmpty()) {
                    wait();
                }
                System.out.println(Thread.currentThread().getName() + " несет заказ");
                int visitorNum = deliveryList.remove(0).getVisitorNum();
                System.out.printf("Посетитель %s приступил к еде", visitorNum);
                Thread.sleep(EAT_TIME);
                System.out.printf("Посетитель %s вышел из ресторана", deliveryList.remove(0).getVisitorNum());
                notifyAll();
                /*if ((orderList.isEmpty() && (coockingList.isEmpty()) && (deliveryList.isEmpty()))) {
                    Thread.currentThread().getThreadGroup().interrupt();
                }*/
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

public class Ex3 {
    public static void main(String[] args) {
        Rest rest = new Rest();
        ThreadGroup mainGroup = new ThreadGroup("Ресторан");
        new Thread(mainGroup, rest::serviceOrders, "Официант 1").start();
        new Thread(mainGroup, rest::serviceOrders, "Официант 2").start();
        new Thread(mainGroup, rest::serviceOrders, "Официант 3").start();
        new Thread(mainGroup, rest::coockingOrder, "Повар").start();
        new Thread(mainGroup, rest::visitorAddOrder, "Посетители").start();
    }
}
