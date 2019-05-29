package mainpkg;

public class LoadingPrinter {

    private boolean isStoped;
    private int ctr;

    public LoadingPrinter(){
        isStoped = false;
        ctr = 0;
    }

    public void printLoadingLine() {
        int ctr = 0;
        System.out.println();
        while(true){
            if(isStoped)
                break;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            System.out.print(".");
            ctr++;
            if(ctr == 10) {
                for (int i = 0; i < ctr; i++) {
                    System.out.print("\b");
                }
                ctr = 0;
            }
        }
    }

    public void stop(){
        isStoped = true;
    }

    public void reset(){
        isStoped = false;
    }
}