public class software_kadai_03 {
    public static void main(String[] args) {
        Book book1 = new Book("A");

        Thread th1 = new Thread(book1, "水野");
        Thread th2 = new Thread(book1, "山口");
        Thread th3 = new Thread(book1, "小林");

        th1.start();
        th2.start();
        th3.start();
    }
}

class ShareMethod {
    void sleep() {
        int random = (int) (Math.random() * 100);
        try {
            Thread.sleep(random);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}

class Book implements Runnable {
  ShareMethod SM = new ShareMethod();
  
  private boolean status; // 貸し出し状態
  private String name; // 貸し出し者の名前
  private static final String EMPTY = ""; // EMPTYを定義
  
  // コンストラクタ
  Book(String nm) {
      status = false;
        name = nm;
    }
  
  synchronized boolean addBook(String nm) {
      SM.sleep();
      synchronized(this){
        if (status == false) {
          status = true;
          name = nm;
          System.out.println(nm + "さん，お貸しします．");
          return true;
        } else {
          System.out.println(nm + "さん，残念ながら貸し出し中となります．");
          return false;
        }
      }
    }
  
  synchronized void retBook(String nm) {
    SM.sleep();
    synchronized(this){
      if (status == true) {
        System.out.println(nm + "さん，返却を承りました．");
        status = false;
        name = EMPTY;
      } else {
        System.out.println(nm + "さん，おかしい！");
      }
    }
  }
  
  @Override
  public void run() {
    for (int i = 0; i < 5; i++) {      
      String currentThreadName = Thread.currentThread().getName();
      boolean borrowed = addBook(currentThreadName);
      if(borrowed) {
        retBook(currentThreadName);
      }
      SM.sleep();
    }
  }
}
