package net.neferett.linaris.sheepwars.handler;

import org.bukkit.ChatColor;

public enum Step {
    LOBBY(true, ChatColor.GREEN + "Attente..."),
    IN_GAME(false, ChatColor.RED + "En jeu"),
    POST_GAME(false, ChatColor.DARK_RED + "Victoire");

    private static Step currentStep;

    public static boolean canJoin() {
        return Step.currentStep.canJoin;
    }

    public static String getMOTD() {
        return Step.currentStep.motd;
    }

    public static boolean isStep(final Step step) {
        return Step.currentStep == step;
    }

    public static void setCurrentStep(final Step currentStep) {
        Step.currentStep = currentStep;
    }

    private boolean canJoin;

    private String motd;

    Step(final boolean canJoin, final String motd) {
        this.canJoin = canJoin;
        this.motd = motd;
    }
}
