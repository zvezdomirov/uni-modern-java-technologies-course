package bg.sofia.uni.fmi.mjt.stylechecker;

public class ViolatedRulesCommenter {
    private ViolatedRulesChecker checker;

    public ViolatedRulesCommenter() {
        this.checker = new ViolatedRulesChecker();
    }

    public String comment(String lineToCheck) {
        return new StringBuilder()
                .append(this.checker.checkLineForOpenBracketAtBeginning(lineToCheck))
                .append(this.checker.checkLineForMoreThanOneStatement(lineToCheck))
                .append(this.checker.checkLineForWrongPackageNaming(lineToCheck))
                .append(this.checker.checkLineForWildCardInImport(lineToCheck))
                .append(this.checker.checkLineForLengthMoreThanHundredChars(lineToCheck))
                .append(lineToCheck)
                .append(System.lineSeparator())
                .toString();
    }
}
