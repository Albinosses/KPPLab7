package backend;

import java.util.ArrayList;

public class BankManager {
    private static BankManager instance;
    private int threadsNumber;
    private int maxWithdrawal;
    private Bank bank;
    public ArrayList<Thread> threads = new ArrayList<>();
    public ArrayList<Client> clients = new ArrayList<>();

    private BankManager(){}

    public static BankManager getInstance() {
        if (instance == null) {
            instance = new BankManager();
        }
        return instance;
    }

    public void RunThreads(){
        for (int i = 1; i <= threadsNumber; i++) {
            Client client = new Client(bank, maxWithdrawal);
            Thread thread = new Thread(client);
            thread.setPriority((int) (Math.random() * (Thread.MAX_PRIORITY - Thread.MIN_PRIORITY + 1)) + Thread.MIN_PRIORITY);
            threads.add(thread);
            clients.add(client);
            thread.start();
        }
    }
    public void initializeBank (int initialBalance){
        this.bank = new Bank(initialBalance);
    }

    public void setThreadsNumber(int threadsNumber){
        this.threadsNumber = threadsNumber;
    }

    public int getThreadsNumber(){
        return this.threadsNumber;
    }

    public void setMaxWithdrawal(int maxWithdrawal){
        this.maxWithdrawal = maxWithdrawal;
    }

    public int getMaxWithdrawal(){
        return this.maxWithdrawal;
    }
}
