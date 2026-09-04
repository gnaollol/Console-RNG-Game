package bossgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;

class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean gameRunning = true;
        boolean keepTrying;

        // boss 1 
        ArrayList<String> playerAttacks = new ArrayList<>(Arrays.asList("Charge", "Explode", "Spell of Weakening"));
        ArrayList<Double> damageNum = new ArrayList<>(Arrays.asList(10.0, 25.0, 45.0));
        double[] bossDmg = {10, 25, 50};
        String[] bossAttacks = {"Spear", "Smash", "Laser"};
        final double BOSS_HEALTH = 250;

        // boss2
        ArrayList<String> playerAttacks2 = new ArrayList<>(Arrays.asList("Emperyical Shield", "Praying Mantis", "Beam of Particles"));
        ArrayList<Double> damageNum2 = new ArrayList<>(Arrays.asList(0.0, 150.0, 250.0));
        double[] bossDmg2 = {100, 200, 300};
        String[] bossAttacks2 = {"Carbondixicator", "Radiating Poison", "Barrage-of-Doom"};
        final double BOSS_HEALTH2 = 800;

        // --- ref to wtv boss is active.
        ArrayList<String> currentPlayerAttacks = playerAttacks;
        ArrayList<Double> currentDamageNum = damageNum;
        double[] currentBossDmg = bossDmg;
        String[] currentBossAttacks = bossAttacks;
        double currentBossHealth = BOSS_HEALTH;
        int bossLevel = 1;

        double coinsEarned = 50;

        Random random = new Random();
        Random health = new Random();
        double playerHealth = health.nextInt(49, 101);
       int attackCount = 0;
        boolean powerUpAdded = false;
        boolean powerUpAdded2 = false;

        System.out.println("Your health is: " + playerHealth);
        for (int i = 0; i < 10; i++) {
            System.out.print("*" + " ");
        }

        if (playerHealth < 30) {
            System.out.println("\nI dont think you will make it out alive... Good luck, here's +5 health to improve your chances of survivng!");
            playerHealth = playerHealth + 5;
        } else {
            System.out.println("\nYou might have a good chance.. Good luck..!");
        }

        while (gameRunning) {
            // --- Player's turn ---
            System.out.println("\nYour attacks: ");
            for (int i = 0; i < currentPlayerAttacks.size(); i++) {
                System.out.print(currentPlayerAttacks.get(i) + ", ");
            }

            System.out.println("\n\nWhat attack would you like to choose?: (0-" + (currentPlayerAttacks.size() - 1) + ") ");
            int playerChoice = input.nextInt();

            if (playerChoice > currentPlayerAttacks.size() - 1 || playerChoice < 0) {
                keepTrying = true;
                while (keepTrying) {
                    System.out.println("Uh, please try again. Thanks! (0-" + (currentPlayerAttacks.size() - 1) + ")");
                    playerChoice = input.nextInt();
                    if (playerChoice >= 0 && playerChoice <= currentPlayerAttacks.size() - 1) {
                        keepTrying = false;
                    }
                }
            }

            String chosenAttackName = currentPlayerAttacks.get(playerChoice);
            double attackDamage = currentDamageNum.get(playerChoice);

            // prototype check
            boolean isPrototype = chosenAttackName.equals("Prototype");
            boolean malfunctioned = false;

            if (isPrototype) {
                int malfunctionRoll = random.nextInt(100); // 0-99
                if (malfunctionRoll < 64) { // 64% chance to malfunction
                    malfunctioned = true;
                }
            }

            if (malfunctioned) {
                System.out.println("\n*** WARNING: Prototype malfunctioned! It fizzles out and deals no damage. ***");
                attackDamage = 0.0;
            }

            // --- Boss 2 shield check (only applies once boss 2 is active) ---
            boolean bossBlocked = false;
            if (bossLevel == 2 && attackDamage > 0) {
                int bossShieldRoll = random.nextInt(100); // 0-99
                if (bossShieldRoll < 46) { // 46% chance the boss shields
                    bossBlocked = true;
                }
            }

            if (bossBlocked) {
                double blockedDamage = attackDamage / 2;
                currentBossHealth = currentBossHealth - blockedDamage;
                System.out.println("\nThe boss raises its own shield, absorbing half your attack!");
                System.out.println("You picked: " + chosenAttackName + ", which deals: " + attackDamage + " (halved to " + blockedDamage + ")");
                System.out.println("You have reduced " + blockedDamage + " from the boss. His health is now at: " + currentBossHealth);
            } else {
                currentBossHealth = currentBossHealth - attackDamage;
                System.out.println("You picked: " + chosenAttackName + ", which deals: " + attackDamage);
                System.out.println("You have reduced " + attackDamage + " from the boss. His health is now at: " + currentBossHealth);
            }

            // Check if the player blocked this turn
            boolean playerBlocked = chosenAttackName.equals("Emperyical Shield");

            // --- Ability cooldown: index 2 or higher takes 5 seconds to recharge ---
            if (playerChoice >= 2) {
                System.out.println("\n" + chosenAttackName + " is powerful and needs time to recharge...");
                for (int cooldown = 5; cooldown >= 1; cooldown--) {
                    System.out.println("Cooldown: " + cooldown + "...");
                    try {
                        Thread.sleep(1000); // 1 second per tick, 5 ticks total = 5 seconds
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.println(chosenAttackName + " is ready again!");
            }

            attackCount++;

            // Unlock power-up after 3 attacks — different unlock per boss
            if (attackCount == 3) {
                if (bossLevel == 1 && !powerUpAdded) {
                    currentPlayerAttacks.add("Zeus Himself");
                    currentDamageNum.add(150.0);
                    powerUpAdded = true;
                    System.out.println("\n*** You unlocked a new attack: Zeus Himself ***");
                } else if (bossLevel == 2 && !powerUpAdded2) {
                    currentPlayerAttacks.add("Prototype");
                    currentDamageNum.add(500.0);
                    powerUpAdded2 = true;
                    System.out.println("\n*** You unlocked a new attack: (DANGER) Prototype is unstable, may malfunction! ***");
                }
            }

            if (currentBossHealth <= 0) {
                if (bossLevel == 1) {
                    System.out.println("\nYou defeated the boss! Congratulations, you win!");
                    coinsEarned = coinsEarned + 100;
                    System.out.println("\nYou have also earned: " + coinsEarned + " coins.");

                    System.out.println("\nAnother boss has appeared...");
                    System.out.println("This boss is gnarly... So I've added a couple extra protection for you!");

                    int bonusHealth = health.nextInt(350, 801); // random value from 350 to 800 inclusive
                    System.out.println("You have gained +" + bonusHealth + " health.");
                    playerHealth = playerHealth + bonusHealth;
                    
                    if (playerHealth > 600 && playerHealth <= 800) {
                        System.out.println("You have been rewarded with level III armor! ");
                    } else if (playerHealth > 400 && playerHealth <= 600) {
                        System.out.println("You have been awarded with level II armor! ");
                    } else if (playerHealth > 300 && playerHealth <= 400) {
                        System.out.println("You have been awarded with level I armor! ");
                    }

                    currentPlayerAttacks = playerAttacks2;
                    currentDamageNum = damageNum2;
                    currentBossDmg = bossDmg2;
                    currentBossAttacks = bossAttacks2;
                    currentBossHealth = BOSS_HEALTH2;
                    bossLevel = 2;

                    attackCount = 0;

                    continue;
                } else {
                    System.out.println("\nYou defeated the FINAL boss! You are victorious!");
                    coinsEarned = coinsEarned + 500;
                    System.out.println("\nYou have earned a total of: " + coinsEarned + " coins.");
                    gameRunning = false;
                    break;
                }
            }

            // --- Boss's turn ---
            int randomIndex = random.nextInt(currentBossAttacks.length);
            String randomAttack = currentBossAttacks[randomIndex];
            double dmgDealt = currentBossDmg[randomIndex];

            // Barrage-of-Doom (or any boss with more than 3 attacks in their list)
            // is too strong to fully block, shield only absorbs half the damage
            boolean partialBlockOnly = randomAttack.equals("Barrage-of-Doom") || currentBossAttacks.length > 3;

            if (playerBlocked && !partialBlockOnly) {
                System.out.println("\nThe boss attempts: " + randomAttack + ", but you raised your Emperyical Shield and blocked it completely!");
                System.out.println("No damage taken. Your health remains at: " + playerHealth);
            } else if (playerBlocked && partialBlockOnly) {
                double reducedDmg = dmgDealt / 2;
                playerHealth = playerHealth - reducedDmg;
                System.out.println("\nThe boss unleashes: " + randomAttack + "! Your shield absorbs some of it, but it's too powerful to fully block.");
                System.out.println("You take " + reducedDmg + " reduced damage. Your health is now: " + playerHealth);
            } else {
                System.out.println("\nUnfortunately, it's his turn to attack you.");
                playerHealth = playerHealth - dmgDealt;
                System.out.println("The boss has picked: " + randomAttack + " which dealt: " + dmgDealt);
                System.out.println("Your health is now: " + playerHealth);
            }

            if (playerHealth <= 0) {
                System.out.println("\nSorry game over, you have died....");
                gameRunning = false;
            } else if (!playerBlocked) {
                System.out.println("Here's +10 health for surviving. ");
                playerHealth = playerHealth + 10;
                System.out.println("Your health is now: " + playerHealth);
            }
        }
    }
}
