import javax.naming.InsufficientResourcesException;

public class Operations {
    public static void main(String[] args) throws InsufficientResourcesException, InterruptedException {
        final Account a = new Account(1000);
        final Account b = new Account(2000);

        new Thread(new Runnable() {
            public void run() {
                try {
                    transfer(a, b, 500);
                } catch (InsufficientResourcesException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Thread.sleep(100);
        transfer(b, a, 300);
    }

    static void transfer(Account acc1, Account acc2, int amount)
            throws InsufficientResourcesException, InterruptedException {
        if(acc1.getLock().tryLock()){
            try {
                if (acc2.getLock().tryLock()) {
                    if (acc1.getBalance() < amount) {
                        System.out.println("Not enough money");
                        throw new InsufficientResourcesException();
                    }
                    acc1.withdraw(amount);
                    System.out.println("Withdraw from " + acc1 + ": " + amount);
                    acc2.deposit(amount);
                    System.out.println("Deposit to " + acc2 + ": " + amount);
                    System.out.println(Thread.currentThread().getName());
                }
            }
            finally {
                acc1.getLock().unlock();
            }
        }else {
            System.out.println("Nope. " + Thread.currentThread().getName());
        }
    }
}
