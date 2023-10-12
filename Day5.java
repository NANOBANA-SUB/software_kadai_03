import java.util.*;

public class Day5{
  public static void main(String[] args){
    //仕様通りの動きはできていそうなので基本問題はないかなと思った．
    //BorrowerクラスでもクラスBookのインスタンスを生成するので結構冗長的に感じる．今回の場合Borrowerクラスを作らず，Threadだけでいいと思う(授業スライドのマルチスレッドの作り方Ⅱの方法)．
    Book book = new Book();
    Borrower stu1 = new Borrower(book, "A");
    Borrower stu2 = new Borrower(book, "B");
    Borrower stu3 = new Borrower(book, "C");
    
    Thread th1 = new Thread(stu1);
    Thread th2 = new Thread(stu2);
    Thread th3 = new Thread(stu3);
    
    th1.start();
    th2.start();
    th3.start();
  }
}

class Book{
  private String borrower = "EMPTY";
  private boolean lending = false;
  private Util utl = new Util();//Utilのインスタンスはクラスの最初で生成
  
  public synchronized boolean addBook(String name){
    /*メソッドが呼ばれる度にインスタンス生成をすると重くなるので最初に生成させた
    Util utl = new Util();
    */
    utl.rand_pause(100);
    if(lending == false){
      this.borrower = name;
      this.lending = true;
      System.out.println(name + "さん、お貸しします");
      return true;
    }else{
      System.out.println(name + "さん、借りられません。" + this.borrower + "さんが貸出中です");
      return false;
    }
  }
  
  public synchronized void retBook(String name){
    /*addBook同様に
    Util utl = new Util();
    */
    utl.rand_pause(100);
    if(lending == true && borrower == name){
      this.borrower = "EMPTY";
      this.lending = false;
      System.out.println(name + "さん、返却しました");
    }else{
      System.out.println(name + "さん、おかしい！");
    }
  }
}

class Borrower implements Runnable{
  Book book;
  String name;
  private Util utl = new Util();
  
  Borrower(Book book){
    this.book = book;
    this.name = "ゲスト";
  }
  
  Borrower(Book book, String name){
    this.book = book;
    this.name = name;
  }
  
  public void run(){
    for(int i=0; i<10; i++){
      if(book.addBook(this.name)){
        book.retBook(this.name);
      }
      utl.rand_pause(100);//スレッドごとに処理のポーズを入れる
    }
  }
}

class Util	{
	void rand_pause(int t) {
		try {
			int n = (int)(Math.random()*t);
			Thread.sleep(n);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
}
