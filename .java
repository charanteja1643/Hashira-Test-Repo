function baseToDecimal(value, base) {
    let result = 0;
    for (let i = 0; i < value.length; i++) {
        const digit = value[i];
        let digitValue;
        if (digit >= '0' && digit <= '9') {
            digitValue = parseInt(digit);
        } else {
            // For bases > 10, letters represent values (a=10, b=11, etc.)
            digitValue = digit.toLowerCase().charCodeAt(0) - 'a'.charCodeAt(0) + 10;
        }
        result = result * base + digitValue;
    }
    return result;
}

function lagrangeInterpolation(points, x) {
    let result = 0;
    const n = points.length;
    
    for (let i = 0; i < n; i++) {
        const [xi, yi] = points[i];
        let li = 1;
        
        for (let j = 0; j < n; j++) {
            if (i !== j) {
                const [xj, yj] = points[j];
                li *= (x - xj) / (xi - xj);
            }
        }
        
        result += yi * li;
    }
    
    return result;
}

function solveShamirSecretSharing(jsonData) {
    const n = jsonData.keys.n;
    const k = jsonData.keys.k;
    
    const points = [];
    for (const key in jsonData) {
        if (key !== 'keys') {
            const x = parseInt(key);
            const base = parseInt(jsonData[key].base);
            const value = jsonData[key].value;
            const y = baseToDecimal(value, base);
            points.push([x, y]);
        }
    }
    
    points.sort((a, b) => a[0] - b[0]);
    
    const selectedPoints = points.slice(0, k);
    
    console.log(Using ${k} points out of ${points.length} available:);
    selectedPoints.forEach(([x, y]) => {
        console.log(`  Point: (${x}, ${y})`);
    });
    const secret = lagrangeInterpolation(selectedPoints, 0);
    
    return Math.round(secret);
}
const testCase1 = {
    "keys": {
        "n": 4,
        "k": 3
    },
    "1": {
        "base": "10",
        "value": "4"
    },
    "2": {
        "base": "2",
        "value": "111"
    },
    "3": {
        "base": "10",
        "value": "12"
    },
    "6": {
        "base": "4",
        "value": "213"
    }
};

const testCase2 = {
    "keys": {
        "n": 10,
        "k": 7
    },
    "1": {
        "base": "6",
        "value": "13444211440455345511"
    },
    "2": {
        "base": "15",
        "value": "aed7015a346d635"
    },
    "3": {
        "base": "15",
        "value": "6aeeb69631c227c"
    },
    "4": {
        "base": "16",
        "value": "e1b5e05623d881f"
    },
    "5": {
        "base": "8",
        "value": "316034514573652620673"
    },
    "6": {
        "base": "3",
        "value": "2122212201122002221120200210011020220200"
    },
    "7": {
        "base": "3",
        "value": "20120221122211000100210021102001201112121"
    },
    "8": {
        "base": "6",
        "value": "20220554335330240002224253"
    },
    "9": {
        "base": "12",
        "value": "45153788322a1255483"
    },
    "10": {
        "base": "7",
        "value": "1101613130313526312514143"
    }
};

// Run test cases
console.log("=== Test Case 1 ===");
const secret1 = solveShamirSecretSharing(testCase1);
console.log(Secret: ${secret1});

console.log("\n=== Test Case 2 ===");
const secret2 = solveShamirSecretSharing(testCase2);
console.log(Secret: ${secret2});
