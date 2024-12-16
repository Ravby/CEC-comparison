% Get the directory of the current script
scriptDir = fileparts(mfilename('fullpath'));

% Define the folder containing the .mat files
folderPath = fullfile(scriptDir, 'RDE'); %BlockEA IEACOP RDE

% Get a list of all .mat files in the folder
matFiles = dir(fullfile(folderPath, '*.mat'));

% Loop through each .mat file
for k = 1:length(matFiles)
    % Get the full path to the .mat file
    matFilePath = fullfile(folderPath, matFiles(k).name);
    
    % Load the .mat file
    data = load(matFilePath);

    Min_EV = data.data; % Min_EV combinedMatrix data

    % Define the output .txt file path
    [~, fileName, ~] = fileparts(matFiles(k).name);
    txtFilePath = fullfile(folderPath, [fileName, '.txt']);

    % Open the .txt file for writing
    fileID = fopen(txtFilePath, 'w');

    % Write the Min_EV array to the .txt file
    [rows, cols] = size(Min_EV);
    for i = 1:1000
        fprintf(fileID, '%g ', Min_EV(i, 1:cols-1));
        fprintf(fileID, '%g\n', Min_EV(i, cols));
    end

    % Close the .txt file
    fclose(fileID);

end
