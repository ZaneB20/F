import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class CardBattleGame {

    
    static abstract class Card {
        protected String name;
        protected int damage;
        protected int shield;

        public Card(String name, int damage, int shield) {
            this.name = name;
            this.damage = damage;
            this.shield = shield;
        }

       
        public abstract void play(Character player, Character enemy);

        
        public String toString() {
            return String.format("%-15s [Damage: %d, Shield: %d]", name, damage, shield);
        }
    }

  
    static class Fighter extends Card {
        public Fighter() {
            super("Fighter", 5, 0);
        }

       
        public void play(Character player, Character enemy) {
            enemy.hp -= damage;
            System.out.println("You played Fighter. Dealt " + damage + " damage to " + enemy.name + ".");
        }
    }

    
    static class Healer extends Card {
        public Healer() {
            super("Healer", -5, 0);
        }

      
        public void play(Character player, Character enemy) {
            
            player.hp -= damage;
            System.out.println("You played Healer. Healed yourself for " + (-damage) + " HP.");
        }
    }

    
    static class ShieldBearer extends Card {
        public ShieldBearer() {
            super("ShieldBearer", 0, 7);
        }

        
        public void play(Character player, Character enemy) {
            player.shield += shield;
            System.out.println("You played ShieldBearer. Gained " + shield + " shield points.");
        }
    }

    
    static class Character {
        String name;
        int hp;
        int shield;

        public Character(String name, int hp) {
            this.name = name;
            this.hp = hp;
            this.shield = 0;
        }

        public boolean isAlive() {
            return hp > 0;
        }

        public void report() {
            System.out.println(name + " HP: " + hp + " | Shield: " + shield);
        }
    }

    
    static class Enemy extends Character {
        int attack;

        public Enemy(String name, int hp, int attack) {
            super(name, hp);
            this.attack = attack;
        }
    }

    
    static class Deck {
        ArrayList<Card> cards;
        Random rand;

        public Deck() {
            cards = new ArrayList<>();
            rand = new Random();
            setDefaultDeck();
        }

        
        private void setDefaultDeck() {
            cards.add(new Fighter());
            cards.add(new Fighter());
            cards.add(new Fighter());
            cards.add(new ShieldBearer());
            cards.add(new ShieldBearer());
            cards.add(new Healer());
        }

       
        public void addRandomCard() {
            int type = rand.nextInt(3);
            if (type == 0) {
                cards.add(new Fighter());
                System.out.println("A new Fighter card has been added to your deck.");
            } else if (type == 1) {
                cards.add(new Healer());
                System.out.println("A new Healer card has been added to your deck.");
            } else {
                cards.add(new ShieldBearer());
                System.out.println("A new ShieldBearer card has been added to your deck.");
            }
        }

       
        public void showCards() {
            for (int i = 0; i < cards.size(); i++) {
                System.out.println(i + ": " + cards.get(i));
            }
        }

        
        public void removeCard(int index) {
            if (index >= 0 && index < cards.size()) {
                cards.remove(index);
            }
        }

        public int size() {
            return cards.size();
        }
    }

   
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean playAgain = true;

        while (playAgain) {
           
            Character player = new Character("Player", 30);
            Deck deck = new Deck();

            
            Enemy[] enemies = {
                new Enemy("Wolf", 10, 2),
                new Enemy("Bear", 15, 4),
                new Enemy("Goblin", 8, 2),
                new Enemy("Elf", 12, 3),
                new Enemy("Evil Wizard", 20, 5)
            };

            System.out.println("\n=== Welcome to the Card Battle Game! ===");

            
            int totalRounds = 0;
            boolean gameOver = false;

           
            for (Enemy enemy : enemies) {
                System.out.println("\nA new enemy appears: " 
                        + enemy.name + " (HP: " + enemy.hp + ", Attack: " + enemy.attack + ")");

               
                while (enemy.hp > 0 && player.hp > 0) {
                    totalRounds++;
                   
                    System.out.println("\n--- New Round " + totalRounds + " ---");
                    
                    System.out.println("Your status:");
                    player.report();
                    System.out.println("Enemy status:");
                    enemy.report();

                   
                    System.out.println("\nYour deck:");
                    deck.showCards();
                    System.out.print("Choose a card to play by index: ");
                    int choice = -1;
                    while (true) {
                        try {
                            choice = Integer.parseInt(scanner.nextLine());
                            if (choice < 0 || choice >= deck.size()) {
                                System.out.print("Invalid choice. Please enter a valid index: ");
                                continue;
                            }
                            break;
                        } catch (NumberFormatException e) {
                            System.out.print("Invalid input. Please enter a number: ");
                        }
                    }

                   
                    Card chosenCard = deck.cards.get(choice);
                    chosenCard.play(player, enemy);
                    deck.removeCard(choice);

                   
                    if (enemy.hp <= 0) {
                        System.out.println(enemy.name + " is defeated!");
                        break;
                    }

                   
                    System.out.println(enemy.name + " attacks!");
                    int effectiveAttack = enemy.attack;
                    if (player.shield > 0) {
                        int blocked = Math.min(player.shield, enemy.attack);
                        effectiveAttack = enemy.attack - blocked;
                        System.out.println("Your shield blocked " + blocked + " damage.");
                        player.shield = Math.max(0, player.shield - enemy.attack);
                    }
                    player.hp -= effectiveAttack;
                    System.out.println("You received " + effectiveAttack + " damage.");

                   
                    if (player.hp <= 0) {
                        System.out.println("You have been defeated by " + enemy.name + "!");
                        gameOver = true;
                        break;
                    }

                   
                    deck.addRandomCard();
                }
                if (gameOver) {
                    break;
                }
            }

           
            if (!player.isAlive()) {
                System.out.println("\nGame Over! You lost.");
            } else {
                System.out.println("\nCongratulations! You defeated all enemies!");
            }

           
            System.out.println("Total rounds played: " + totalRounds);

           
            System.out.print("\nWould you like to play again? (y/n): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            if (!answer.equals("y") && !answer.equals("yes")) {
                playAgain = false;
            }
        }

        System.out.println("Thank you for playing!");
        scanner.close();
    }
}
