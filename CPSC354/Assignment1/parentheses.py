def parentheses(expression: str) -> str:
    stack = []
    for char in expression:
        if char == '(':
            stack.append(char)
        elif char == ')':
            if not stack:
                return "no"
            stack.pop()
    return "yes" if not stack else "no"

if __name__ == "__main__":
    import sys 
    print(parentheses(sys.argv[1]))
