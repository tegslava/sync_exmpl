class Shop {
    // Продавец
    final Object seller = new Object();

    public void buySomething() {
        // Критическая секция
        synchronized (seller) {
            System.out.println(Thread.currentThread().getName() + " покупает");
            // Процесс покупки. Эмулируем, приостанавливая поток
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " пошел домой");
        }
    }
}

public class Ex1 {
    public static void main(String[] args) throws InterruptedException {
        final Shop shop = new Shop();
        // Идем за покупками
        Runnable shopping = shop::buySomething;
        new Thread(null, shopping, "Покупатель 1").start();
        new Thread(null, shopping, "Покупатель 2").start();
        new Thread(null, shopping, "Покупатель 3").start();
    }
}
