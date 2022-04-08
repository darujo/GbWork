package java2.homework1;

public class Cat implements Participant{
    public Cat(int num, int maxRun, int maxJump) {
        this.name = "People " + num;
        this.maxJump = maxJump;
        this.maxRun = maxRun;
    }

    private final String  name;
    private final int maxJump;
    private final int maxRun;

    @Override
    public boolean jump(int height) {
        boolean ok = height < maxJump;
        if(ok){
            System.out.println(name + " перепрыгнул перпятствие высотой "  + height);
        } else {
            System.out.println(name + " не смог перепрыгнуть перпятствие высотой "  + height);
        }
        return ok;

    }

    @Override
    public boolean run(int length) {
        boolean ok = length < maxRun;
        if(ok){
            System.out.println(name + " пробежал "  + length);
        } else {
            System.out.println(name + " не смог пробежать "  + length);
        }
        return ok;

    }

    @Override
    public String toString() {
        return "Я " + name + " могу пробежать " + maxRun + " и могу прыгнуть " + maxJump;
    }
}
