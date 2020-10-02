import java.util.ArrayList;
import java.util.List;

class Shop2 {
    // Продавец
    Seller seller = new Seller(this);
    List<Bread> breads = new ArrayList<>(10);

    public Bread sellBread() {
        return seller.sellBread();
    }

    public void acceptBread() {
        seller.receiveBread();
    }

    List<Bread> getBreads() {
        return breads;
    }
}

class Seller {
    final private Shop2 shop2;

    public Seller(Shop2 shop2) {
        this.shop2 = shop2;
    }

    public synchronized Bread sellBread() {
        try {
            System.out.println("Продавец: Продаю хлеб");
            while (shop2.getBreads().size() == 0) {
                System.out.println("Продавец: Не могу продать - хлеба нет!");
                wait();
            }
            Thread.sleep(1000);
            System.out.println("Продавец: Продано");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return shop2.getBreads().remove(0);
    }


    public synchronized void receiveBread() {
        try {
            System.out.println("Продавец: Принимаю товар");
            Thread.sleep(3000);
            shop2.getBreads().add(new Bread());
            System.out.println("Продавец: Прием товара завершен");
            notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Bread {
}

public class Ex2 {
    public static void main(String[] args) {
        final Shop2 shop2 = new Shop2();
        // Покупатель, чья работа заключается в том, чтобы купить хлеб - shop.sellBread()
        new Thread(null, shop2::sellBread, "Покупатель").start();
        // Водитель хлебопекарни, чья работа заключается в том, чтобы привезти хлеб - shop.acceptBread()
        new Thread(null, shop2::acceptBread, "Водитель хлебопекарни").start();
    }
}
