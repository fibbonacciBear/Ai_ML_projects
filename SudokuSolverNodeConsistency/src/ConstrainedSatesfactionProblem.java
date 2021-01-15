
import java.util.HashSet;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author akashganesan
 */
public class ConstrainedSatesfactionProblem {

    public HashSet[][] domains;
    int m;
    int n;

    public ConstrainedSatesfactionProblem(int m) {
        this.m = m;
        this.n = m * m;
        this.domains = new HashSet[n][n];
        initializeDomain();
    }

    public ConstrainedSatesfactionProblem(ConstrainedSatesfactionProblem problem) {
        this.m = problem.m;
        this.n = problem.n;
        this.domains = new HashSet[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                HashSet set = problem.domains[i][j];
                this.domains[i][j] = (HashSet) set.clone();
            }
        }
        //domains;
    }

    private void initializeDomain() {
        domains = new HashSet[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                domains[i][j] = new HashSet();
                for (int l = 1; l <= n; l++) {
                    domains[i][j].add(l);
                }
            }
        }
    }

    public int[] findMRV() {
        int size = Integer.MAX_VALUE;
        int[] out = new int[2];
        out[0] = -1;
        out[1] = -1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int dsize = domains[i][j].size();
                if ((size > dsize) && (dsize != 1)) {
                    size = domains[i][j].size();
                    out[0] = i;
                    out[1] = j;
                }
            }
        }
        return out;
    }

    public void printDomain() {
        for (int row = 0; row < n; row++) {
            if (row > 0 && row % m == 0) {
                for (int col = 0; col < n; col++) {
                    if (col > 0 && col % m == 0) {
                        System.out.print("+-");
                    }
                    System.out.print("---");
                }
                System.out.println();
            }
            for (int col = 0; col < n; col++) {
                Set value = this.domains[row][col];
                if (col > 0 && col % m == 0) {
                    System.out.print("| ");
                }
                System.out.print(value.toString());
            }
            System.out.println();
        }

    }

    public void findPeers(int row, int col) {
        for (int i = 0; i < domains.length; i++) {

            if (i != row) {
                System.out.println(i + "," + col);
            }
        }
        for (int j = 0; j < domains.length; j++) {
            if (j != col) {
                System.out.println(row + "," + j);
            }
        }
        int rowCorner = ((int) (row / m)) * m;
        int colCorner = ((int) (col / m)) * m;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if ((!(row == rowCorner + i || col == colCorner + j))) {
                    System.out.println((rowCorner + i) + "," + (colCorner + j));
                }
            }
        }

    }

    public boolean SetDomainAndChangePeersDomain(int row, int col, int val) {
        for (int i = 1; i <= n; i++) {
            domains[row][col] = new HashSet();
            domains[row][col].add(val);

        }
        // Fix up Peers
        for (int i = 0; i < domains.length; i++) {

            if (i != row) {
                if (domains[i][col].remove(val)) {

                    if (domains[i][col].size() == 0) {
                        return false;
                    }
                    if (domains[i][col].size() == 1) {
                        if (!SetDomainAndChangePeersDomain(i, col, (int) domains[i][col].iterator().next())) {
                            return false;
                        }
                    }
                }
            }
        }
        for (int j = 0; j < domains.length; j++) {
            if (j != col) {
                if (domains[row][j].remove(val)) {
                    if (domains[row][j].size() == 0) {
                        return false;
                    }
                    if (domains[row][j].size() == 1) {
                        if (!SetDomainAndChangePeersDomain(row, j, (int) domains[row][j].iterator().next())) {
                            return false;
                        }
                    }
                }

            }
        }
        int rowCorner = ((int) (row / m)) * m;
        int colCorner = ((int) (col / m)) * m;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                if ((!(row == rowCorner + i || col == colCorner + j))) {
                    if (domains[(rowCorner + i)][(colCorner + j)].remove(val)) {
                        if (domains[rowCorner + i][colCorner + j].size() == 0) {
                            return false;
                        }
                        if (domains[rowCorner + i][colCorner + j].size() == 1) {
                            if (!SetDomainAndChangePeersDomain(rowCorner + i, colCorner + j, (int) domains[rowCorner + i][colCorner + j].iterator().next())) {
                                return false;
                            }
                        }
                    }

                }
            }
        }
        return true;

    }

}
