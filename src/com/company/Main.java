package com.company;

import java.util.Random;

public class Main {

    public static int bossHealth = 2000;
    public static int bossDamage = 50;
    public static String bossDefenceType;
    public static int[] heroesHealth = {260, 210, 270, 500, 220, 230, 300};
    public static int[] heroesDamage = {25, 15, 10, 5, 20, 30, 35};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int roundNumber;
    public static int medicHealth = 500;
    public static int medicTreatment;
    public static int blockedHits;
    public static boolean isBossOut = false;

    public static void main(String[] args) {
        printStatistics();

        while (!isGameFinished()) {
            round();
        }
    }

    public static void chooseBossDefence() {
        Random random = new Random();
        int randomIndex = random.nextInt(heroesAttackType.length); // 0,1,2
        bossDefenceType = heroesAttackType[randomIndex];
        System.out.println("Boss chose: " + bossDefenceType);
    }

    public static void round() {
        roundNumber++;
        System.out.println("\n" + roundNumber + " ROUND -------------------");
        bossHits();
        chooseBossDefence();
        heroesHit();
        setMedicTreament();
        printStatistics();
    }

    public static void bossHits() {
        if (!isBossOut && bossHealth > 0) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] > 0) {
                    if (i == 3) {
                        heroesHealth[i] -= bossDamage;
                    } else {
                        if (i == 4) {
                            Random random = new Random();
                            boolean isLucky = random.nextBoolean();
                            if (!isLucky) {
                                heroesHealth[i] -= bossDamage * 4 / 5;
                                heroesHealth[3] -= bossDamage / 5;
                            } else {
                                System.out.println("Lucky dodged boss damage");
                            }
                        } else {
                            if (i == 5) {
                                Random random = new Random();
                                blockedHits = bossDamage * random.nextInt(101) / 100;
                                System.out.println("Blocked hits by Berserk: " + blockedHits);
                                heroesHealth[i] -= (bossDamage - blockedHits) * 4 / 5;
                                heroesHealth[3] -= bossDamage / 5;
                            } else {
                                heroesHealth[i] -= bossDamage * 4 / 5;
                                heroesHealth[3] -= bossDamage / 5;
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < heroesHealth.length; i++) {
                if (heroesHealth[i] < 0) {
                    heroesHealth[i] = 0;
                }
            }
            if (medicHealth > 0) {
                medicHealth -= bossDamage;
                if (medicHealth < 0) {
                    medicHealth = 0;
                }
            }
        } else {
            System.out.println("Boss is Out.");
        }
    }

    public static void heroesHit() {
        for (int i = 0; i < heroesDamage.length; i++) {
            if (bossHealth > 0 && heroesHealth[i] > 0) {
                if (bossDefenceType.equals(heroesAttackType[i])) {
                    Random random = new Random();
                    int coeff = random.nextInt(9) + 2; // 2,3,4,5,6,7,8,9,10
                    if (((bossHealth - heroesDamage[i]) * coeff < 0) || ((bossHealth - heroesDamage[i] * coeff - blockedHits) < 0)) {
                        bossHealth = 0;
                    } else {
                        if (i != 5) {
                            if (i == 6) {
                                if (isBossOut) {
                                    isBossOut = false;
                                } else {
                                    isBossOut = random.nextBoolean();
                                }
                                if (!isBossOut && heroesHealth[6] > 0) {
                                    bossHealth = bossHealth - heroesDamage[i] * coeff;
                                } else {
                                    isBossOut = true;
                                }
                            } else {
                                bossHealth = bossHealth - heroesDamage[i] * coeff;
                            }
                        } else {
                            bossHealth = bossHealth - heroesDamage[i] * coeff - blockedHits;
                        }
                    }
                    System.out.println("Critical damage: " + heroesDamage[i] * coeff);
                } else {
                    if (((bossHealth - heroesDamage[i]) < 0) || ((bossHealth - heroesDamage[i] - blockedHits) < 0)) {
                        bossHealth = 0;
                    } else {
                        if (i != 5) {
                            if (i == 6) {
                                Random random = new Random();
                                if (isBossOut) {
                                    isBossOut = false;
                                } else {
                                    isBossOut = random.nextBoolean();
                                }
                                if (!isBossOut && heroesHealth[6] > 0) {
                                    bossHealth = bossHealth - heroesDamage[i];
                                } else {
                                    isBossOut = true;
                                }
                            } else {
                                bossHealth = bossHealth - heroesDamage[i];
                            }
                        } else {
                            bossHealth = bossHealth - heroesDamage[i] - blockedHits;
                        }
                    }
                }
            }
        }
    }

    public static boolean isGameFinished() {
        if (bossHealth <= 0) {
            System.out.println("\n Heroes won!!!");
            return true;
        }
        boolean allHeroesDead = true;
        for (int health : heroesHealth) {
            if (health > 0) {
                allHeroesDead = false;
                break;
            }
        }
        if (allHeroesDead) {
            System.out.println("\n Boss won!!!");
        }
        return allHeroesDead;
    }

    public static void printStatistics() {
        System.out.println("\n**** Health Statistics ****");
        if (bossHealth > 0) {
            System.out.println("Boss health: " + bossHealth + " (" + bossDamage + ")");
        } else {
            System.out.println("Boss health: " + bossHealth + " (" + 0 + ")");
        }
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0) {
                System.out.println(heroesAttackType[i] + " health: "
                        + heroesHealth[i] + " (" + heroesDamage[i] + ")");
            } else {
                System.out.println(heroesAttackType[i] + " health: "
                        + heroesHealth[i] + " (" + 0 + ")");
            }
        }
        if (medicHealth > 0) {
            System.out.println("Medic health: " + medicHealth + " (" + medicTreatment + ")");
        } else {
            System.out.println("Medic health: " + medicHealth + " (" + 0 + ")");
        }
        System.out.println("***************************");
    }

    public static void setMedicTreament() {
        Random random = new Random();
        boolean isTreatNeeds = false;
        int lowHealthHeroIndex = 0;
        int lowHeroHealth = 0;
        for (int i = 0; i < heroesHealth.length; i++) {
            if (heroesHealth[i] > 0 && heroesHealth[i] < 100) {
                isTreatNeeds = true;
                if (lowHeroHealth < heroesHealth[i]) {
                    lowHealthHeroIndex = i;
                    lowHeroHealth = heroesHealth[i];
                }
            }
        }
        if (medicHealth > 0 && bossHealth > 0 && isTreatNeeds) {
            medicTreatment = (random.nextInt(10) + 1) * 5;
            System.out.println(heroesAttackType[lowHealthHeroIndex] + " health before Treatment: " + heroesHealth[lowHealthHeroIndex]);
            heroesHealth[lowHealthHeroIndex] += medicTreatment;
        } else medicTreatment = 0;
    }
}

