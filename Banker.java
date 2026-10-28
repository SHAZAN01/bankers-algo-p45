import java.io.*;
import java.util.*;

public class Banker {
    static int n, m;
    static int[] avail;
    static int[][] max, alloc, need;

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("need args");
            return;
        }
        
        m = args.length;
        avail = new int[m];
        for (int i = 0; i < m; i++) avail[i] = Integer.parseInt(args[i]);

        ArrayList<int[]> temp = new ArrayList<>();
        
        // load max file
        try {
            Scanner sc = new Scanner(new File("max.txt"));
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                
                String[] parts = line.split(",");
                int[] row = new int[m];
                for (int j = 0; j < m; j++) {
                    row[j] = Integer.parseInt(parts[j].trim());
                }
                temp.add(row);
            }
            sc.close();
        } catch (Exception e) {
            System.out.println("error reading max.txt");
            return;
        }

        n = temp.size();
        max = new int[n][m];
        alloc = new int[n][m];
        need = new int[n][m];
        
        // setup tables
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                max[i][j] = temp.get(i)[j];
                need[i][j] = max[i][j];
                alloc[i][j] = 0;
            }
        }

        System.out.println("started - " + n + " processes, " + m + " resources");
        Scanner in = new Scanner(System.in);
        
        // main loop
        while (true) {
            System.out.print("> ");
            if (!in.hasNextLine()) break;
            
            String[] cmd = in.nextLine().trim().split(" ");
            if (cmd[0].isEmpty()) continue;
            if (cmd[0].equals("quit")) break;
            
            if (cmd[0].equals("*")) {
                printAll();
                continue;
            }

            if (cmd.length != m + 2) {
                System.out.println("bad input length");
                continue;
            }

            int p = Integer.parseInt(cmd[1]);
            int[] req = new int[m];
            for (int j = 0; j < m; j++) req[j] = Integer.parseInt(cmd[j + 2]);

            if (cmd[0].equals("RQ")) {
                request(p, req);
            } else if (cmd[0].equals("RL")) {
                release(p, req);
            } else {
                System.out.println("unknown command");
            }
        }
        in.close();
    }

    static void request(int p, int[] req) {
        // basic checks
        for (int j = 0; j < m; j++) {
            if (req[j] > need[p][j]) {
                System.out.println("denied: more than needed");
                return;
            }
            if (req[j] > avail[j]) {
                System.out.println("denied: not enough avail");
                return;
            }
        }

        // pretend to give it
        for (int j = 0; j < m; j++) {
            avail[j] -= req[j];
            alloc[p][j] += req[j];
            need[p][j] -= req[j];
        }

        if (isSafe()) {
            System.out.println("granted");
        } else {
            // rollback
            for (int j = 0; j < m; j++) {
                avail[j] += req[j];
                alloc[p][j] -= req[j];
                need[p][j] += req[j];
            }
            System.out.println("denied: unsafe");
        }
    }

    static void release(int p, int[] rel) {
        for (int j = 0; j < m; j++) {
            if (rel[j] > alloc[p][j]) {
                System.out.println("cant release that much");
                return;
            }
        }
        for (int j = 0; j < m; j++) {
            avail[j] += rel[j];
            alloc[p][j] -= rel[j];
            need[p][j] += rel[j];
        }
        System.out.println("released");
    }

    // bankers algorithm
    static boolean isSafe() {
        int[] work = avail.clone();
        boolean[] finish = new boolean[n];
        int count = 0;
        
        boolean found;
        do {
            found = false;
            for (int i = 0; i < n; i++) {
                if (!finish[i]) {
                    boolean canRun = true;
                    for (int j = 0; j < m; j++) {
                        if (need[i][j] > work[j]) canRun = false;
                    }
                    
                    if (canRun) {
                        for (int j = 0; j < m; j++) work[j] += alloc[i][j];
                        finish[i] = true;
                        count++;
                        found = true;
                    }
                }
            }
        } while (found);
        
        return count == n;
    }

    static void printAll() {
        System.out.print("avail: ");
        for (int x : avail) System.out.print(x + " ");
        System.out.println();
        
        System.out.println("max:");
        printMat(max);
        System.out.println("alloc:");
        printMat(alloc);
        System.out.println("need:");
        printMat(need);
    }

    static void printMat(int[][] mat) {
        for (int i = 0; i < n; i++) {
            System.out.print(" p" + i + ": ");
            for (int j = 0; j < m; j++) System.out.print(mat[i][j] + " ");
            System.out.println();
        }
    }
}
