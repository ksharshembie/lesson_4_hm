package com.company;

import java.util.Random;

public class Main {

    public static int bossHealth = 1750;
    public static int bossDamage = 50;
    public static String bossDefenceType;
    public static int[] heroesHealth = {260, 210, 270, 500, 220, 230, 300};
    public static int[] heroesDamage = {25, 15, 10, 5, 20, 30, 40};
    public static String[] heroesAttackType = {"Physical", "Magical", "Kinetic", "Golem", "Lucky", "Berserk", "Thor"};
    public static int roundNumber;
    public static int medicHealth = 300;
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
        if (!isBossOut) {
            for (int i = 0; i < heroesHealth.length; i++) {
                if (bossHealth > 0 && heroesHealth[i] > 0) {
                    if (heroesHealth[i] - bossDamage < 0) {
                        heroesHealth[i] = 0;
                    } else {
                        if (i != 3) {
                            if (i == 4) {
                                Random random = new Random();
                                boolean isLucky = random.nextBoolean();
                                if (isLucky) {
                                    System.out.println(heroesAttackType[i] + " dodged boss's hits");
                                    continue;
                                } else {
                                    if (heroesHealth[3] > 0) {
                                        heroesHealth[i] = heroesHealth[i] - bossDamage * 4 / 5;
                                    } else {
                                        heroesHealth[i] = heroesHealth[i] - bossDamage;
                                    }
                                }
                            } else {
                                if (i == 5) {
                                    Random random = new Random();
                                    int blockRate = random.nextInt(101);
                                    if (heroesHealth[3] > 0) {
                                        blockedHits = bossDamage * blockRate / 100;
                                        //System.out.println("Block Rate: " + blockRate + "%");
                                        System.out.println("Blocked Hits by Berserk: " + blockedHits);
                                        heroesHealth[i] = heroesHealth[i] - (bossDamage - blockedHits) * 4 / 5;
                                    } else {
                                        heroesHealth[i] = heroesHealth[i] - bossDamage;

                                    }
                                } else {
                                    if (heroesHealth[3] > 0) {
                                        heroesHealth[i] = heroesHealth[i] - bossDamage * 4 / 5;
                                    } else {
                                        heroesHealth[i] = heroesHealth[i] - bossDamage;
                                    }
                                }
                            }
                            if (heroesHealth[3] - bossDamage / 5 < 0) {
                                heroesHealth[3] = 0;
                            } else {
                                heroesHealth[3] = heroesHealth[3] - bossDamage / 5;
                            }
                        } else {
                            heroesHealth[i] = heroesHealth[i] - bossDamage;
                        }
                    }
                }
            }
            if (bossHealth > 0 && medicHealth > 0) {
                if (medicHealth - bossDamage < 0) {
                    medicHealth = 0;
                } else {
                    medicHealth -= bossDamage;
                }
            }
        } else {
            System.out.println("Boss is Out");
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
                                isBossOut = random.nextBoolean();
                                if (!isBossOut) {
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
                                isBossOut = random.nextBoolean();
                                if (!isBossOut) {
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
                lowHealthHeroIndex = i;
                lowHeroHealth = heroesHealth[i];
            }
            if (i > 0 && isTreatNeeds && (lowHeroHealth > heroesHealth[i])) {
                lowHealthHeroIndex = i;
                lowHeroHealth = heroesHealth[i];
            }
        }
        if (medicHealth > 0 && bossHealth > 0 && isTreatNeeds) {
            medicTreatment = random.nextInt(11) * 10;
            System.out.println(heroesAttackType[lowHealthHeroIndex] + " health before Treatment: " + heroesHealth[lowHealthHeroIndex]);
            heroesHealth[lowHealthHeroIndex] += medicTreatment;
        } else medicTreatment = 0;
    }
}

