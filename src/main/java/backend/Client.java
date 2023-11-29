package backend;

import java.util.Random;

public class Client implements Runnable{
    private final Object lock = new Object();
    private final Bank bank;
    private final int maxWithdrawal;
    private volatile boolean suspended = false;
    private volatile Thread.State previousState = Thread.State.NEW;
    private volatile long stateChangeTimestamp = -1;

    public Client(Bank bank, int maxWithdrawal) {
        this.bank = bank;
        this.maxWithdrawal = maxWithdrawal;
    }

    public void run() {
        Random random = new Random();

        while (true) {
            try {
                synchronized (lock) {
                    while (suspended) {
                        lock.wait();
                    }
                }

                int amount = random.nextInt(maxWithdrawal) + 1;

                if (random.nextBoolean()) {
                    bank.withdrawCash(amount);
                } else {
                    bank.depositCash(amount);
                }

                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void suspendThread() {
        suspended = true;
    }

    public void resumeThread() {
        synchronized (lock) {
            suspended = false;
            lock.notify();
        }
    }
}
