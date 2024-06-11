/*
 * synchronized 同步處理
 * 即一次只允許一個執行緒進行處理，而其他的執行緒必須等待上個執行緒處理完後才可以進入處理。
 * 
 * ex以下code
 * p1,p2是兩個thread
 * 同時啟動會造成 function add 出錯
 * synchronized使一個add完之後,才能接下一個
 */

package com.martio.game;
class CDonate
{
   private static int sum=0;
   public synchronized static void add(int n)
   {
      int tmp=sum;
      tmp=tmp+n;
      try
      {
         Thread.sleep((int)(1000*Math.random()));
      }
      catch(InterruptedException e){}
      sum=tmp;
      System.out.println("捐款總額= "+sum);
  }
}
class CPerson extends Thread
{
   public void run()
   {
      for(int i=1;i<=3;i++)
         CDonate.add(100);
   }
}
public class test
{
   public static void main(String args[])
   {
      CPerson p1=new CPerson();
      CPerson p2=new CPerson();
      p1.start();
      p2.start();
   }
}
