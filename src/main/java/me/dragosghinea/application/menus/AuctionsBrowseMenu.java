package me.dragosghinea.application.menus;

import me.dragosghinea.model.User;
import me.dragosghinea.model.abstracts.Auction;
import me.dragosghinea.services.AuditService;
import me.dragosghinea.services.enums.AuditAction;
import me.dragosghinea.services.impl.AuditServiceImpl;

import java.util.*;

public class AuctionsBrowseMenu implements Menu {
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public Scanner getInputSource() {
        return scanner;
    }

    private final List<Auction> auctions;

    private final User user;
    private final String sortName;

    private static final int PER_PAGE = 2;
    private int page = 0;
    private final Comparator<Auction> comparator;

    private static final AuditService auditService = AuditServiceImpl.getInstance();

    public AuctionsBrowseMenu(User user, Collection<Auction> auctions, Comparator<Auction> comparator, String sortName) {
        auditService.logInfoAction(AuditAction.AUCTION_BROWSING, "User "+user.getUserDetails().getUsername()+" is browsing auctions with sorting "+sortName, user.getUserDetails().getUsername());
        this.user = user;
        this.auctions = new ArrayList<>(auctions);
        this.comparator = comparator;
        this.auctions.sort(comparator);
        this.sortName = sortName;
    }


    @Override
    public void receiveInput(String input) {
        OPTION op = OPTION.getById(input);

        switch (op) {
            case PREVIOUS -> {
                if (page == 0) {
                    getOutputSource().println("No previous page.");
                    return;
                }

                page--;
            }
            case NEXT -> {
                if (auctions.size() <= (page + 1) * PER_PAGE) {
                    getOutputSource().println("No next page.");
                    return;
                }

                page++;
            }
            case CHECKOUT -> {
                getOutputSource().println("Enter the auction index to check it out: ");
                String nrString = getInputSource().nextLine();
                while(true){
                    try{
                        int nr = Integer.parseInt(nrString);
                        if(nr < 1 || nr > auctions.size()){
                            getOutputSource().println("Index out of bounds, reenter: ");
                            continue;
                        }

                        new AuctionViewMenu(user, auctions.get(nr-1)).start();
                        this.auctions.sort(comparator);
                        break;
                    }catch(Exception x){
                        getOutputSource().println("That is not a valid integer.");
                        break;
                    }
                }
            }
            default -> {
                getOutputSource().println("Unknown option '" + input + "'!");
            }
        }
    }

    @Override
    public String menuOptions() {
        StringBuilder sBuilder = new StringBuilder()
                .append("  Browsing auctions (")
                .append(sortName).append(")\n");

        int pos = page*PER_PAGE;
        for(Auction auction : auctions.stream().skip(page*PER_PAGE).limit(PER_PAGE).toList()){
            sBuilder.append("#").append(++pos)
                    .append(" - ").append(auction).append("\n");
        }

        sBuilder.append("----- Page ").append(page+1).append(" -----\n");
        sBuilder.append("Type 'checkout' to view an auction\n");
        sBuilder.append("Type 'exit' to go back\n");
        sBuilder.append("Navigate with '<' and '>'\n");

        return sBuilder.toString();
    }

    private enum OPTION {
        PREVIOUS("<"),
        NEXT(">"),
        CHECKOUT("checkout"),
        UNKNOWN("");

        private final String id;

        OPTION(String id) {
            this.id = id;
        }

        public static OPTION getById(String id) {
            for (OPTION option : values())
                if (option.id.equals(id))
                    return option;
            return UNKNOWN;
        }
    }
}
