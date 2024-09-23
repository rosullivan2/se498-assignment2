import sys
import re

def parse_expression(expression: str) -> float:
    def evaluate(tokens: list) -> float:
        def parse_expression():
            result = parse_term()
            while tokens and tokens[0] in ('+', '-'):
                op = tokens.pop(0)
                if op == '+':
                    result += parse_term()
                elif op == '-':
                    result -= parse_term()
            return result

        def parse_term():
            term = parse_factor()
            while tokens and tokens[0] in ('*', '/'):
                op = tokens.pop(0)
                if op == '*':
                    term *= parse_factor()
                elif op == '/':
                    term /= parse_factor()
            return term

        def parse_factor():
            factor = parse_power()
            return factor

        def parse_power():
            base = parse_primary()
            while tokens and tokens[0] == '^':
                tokens.pop(0)
                base **= parse_primary()
            return base

        def parse_primary():
            if tokens[0] == '(':
                tokens.pop(0)
                value = parse_expression()
                if tokens and tokens[0] == ')':
                    tokens.pop(0)
                return value
            else:
                return float(tokens.pop(0))

        return parse_expression()

    tokens = tokenize(expression)
    return evaluate(tokens)

def tokenize(expression: str) -> list:
    return re.findall(r'[\d.]+|[()+*/^-]', expression)

if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: python calculator.py '<expression>'")
    else:
        expr = sys.argv[1]
        result = parse_expression(expr)
        print(f"Result: {result}")
