
fn main() {
    let condition = true;
    let number = if !condition { 5 } else { 6 };// return types of both if and else blocks should be same.

    println!("The value of number is: {}",number);
// comment
   

    if number % 4 == 0 {
        println!("number is divisible by 4");
    } else if number % 3 == 0 {
        println!("number is divisible by 3");
    } else if number % 2 == 0 {
        println!("number is divisible by 2");
    } else {
        println!("number is not divisible by 4, 3, or 2");
    }
    /*Using too many else if expressions can clutter your code, so if you have more than one, 
    you might want to refactor your code. 
    Chapter 6 describes a powerful Rust branching construct called match for these cases.*/

}