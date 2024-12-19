import aoc.Day;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class Day17 extends Day {

    @Override
    protected Long partOne(List<String> input) {
        var device = Device.fromInput(input);
        var output = device.runProgram(false);
        System.out.println(output.stream().map(String::valueOf).collect(Collectors.joining(",")));
        return null;
    }

    @Override
    protected Long partTwo(List<String> input) {
        return null;

//        var d = Device.fromInput(input);
//        long min = (long) Math.pow(8, 15);
//        long max = (long) Math.pow(8, 16);
//        long result = -1;
//
//        for (long i = min; i <= max; ++i) {
//            if (d.isQuine(i)) {
//                result = i;
//                break;
//            }
//
//            if ((i % 1_000_000_000) == 0) {
//                System.out.println(i);
//            }
//        }
//
//        return result;
    }

    static class Device {
        private long a;
        private long b;
        private long c;
        private Integer[] program;

        static Device fromInput(List<String> input) {
            var device = new Device();

            device.a = Long.parseLong(input.get(0).split(":")[1].trim());
            device.b = Long.parseLong(input.get(1).split(":")[1].trim());
            device.c = Long.parseLong(input.get(2).split(":")[1].trim());
            device.program = Arrays.stream(input.get(4).split(":")[1].trim().split(","))
                    .map(Integer::parseInt)
                    .toArray(Integer[]::new);

            return device;
        }

        List<Integer> runProgram(boolean requireQuine) {
            int ip = 0;
            var output = new ArrayList<Integer>();

            while (ip < program.length) {
                int instruction = program[ip];
                int operand = program[ip + 1];

                switch (instruction) {
                    case 0:
                        a = (long) (a / Math.pow(2, asCombo(operand)));
                        ip += 2;
                        break;

                    case 1:
                        b = b ^ asLiteral(operand);
                        ip += 2;
                        break;

                    case 2:
                        b = asCombo(operand) % 8;
                        ip += 2;
                        break;

                    case 3:
                        if (a != 0) {
                            ip = (int) asLiteral(operand);
                        } else {
                            ip += 2;
                        }
                        break;

                    case 4:
                        b = b ^ c;
                        ip += 2;
                        break;

                    case 5:
                        output.add((int) (asCombo(operand) % 8));
                        if (requireQuine && (output.size() > program.length || !Objects.equals(output.getLast(), program[output.size() - 1]))) {
                            return List.of();
                        }
                        ip += 2;
                        break;

                    case 6:
                        b = (long) (a / Math.pow(2, asCombo(operand)));
                        ip += 2;
                        break;

                    case 7:
                        c = (long) (a / Math.pow(2, asCombo(operand)));
                        ip += 2;
                        break;
                }
            }

            return output;
        }

        boolean isQuine(long a) {
            long b = 0;
            long c = 0;
            int p = 0;

            while (true) {
                b = a % 8;
                b = b ^ 5;
                c = a / pow2(b);
                b = b ^ 6;
                b = b ^ c;

                if (b % 8 != program[p++]) {
                    return false;
                }

                a = a / 8;

                if (a == 0) {
                    return p == program.length;
                }
                if (p >= program.length) {
                    return false;
                }
            }
        }

        long pow2(long b) {
            return switch ((int) b) {
                case 0 -> 1;
                case 1 -> 2;
                case 2 -> 4;
                case 3 -> 8;
                case 4 -> 16;
                case 5 -> 32;
                case 6 -> 64;
                case 7 -> 128;
                default -> throw new RuntimeException("Unexpected pow2 value: " + b);
            };
        }

        long asLiteral(int operand) {
            return operand;
        }

        long asCombo(int operand) {
            return switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> a;
                case 5 -> b;
                case 6 -> c;
                default -> throw new RuntimeException("Illegal combo operand: " + operand);
            };
        }
    }

}
