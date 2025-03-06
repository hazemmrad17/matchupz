from utils import process_video
from tracking import ObjectTracker, KeypointsTracker
from club_assignment import ClubAssigner, Club
from ball_to_player_assignment import BallToPlayerAssigner
from annotation import FootballVideoProcessor
import numpy as np
import os
import tkinter as tk
from tkinter import filedialog, messagebox

class FootballAnalysisGUI:
    def __init__(self, root):
        self.root = root
        self.root.title("Football Video Analysis")
        self.root.geometry("400x300")  # Smaller window since we have fewer fields

        # Variables to store selections
        self.video_path = tk.StringVar()
        self.output_path = tk.StringVar()
        self.batch_size = tk.IntVar(value=10)

        # Create GUI elements
        self.create_widgets()

    def create_widgets(self):
        # Main frame for padding
        main_frame = tk.Frame(self.root, padx=20, pady=20)
        main_frame.pack(fill="both", expand=True)

        # Title
        tk.Label(main_frame, text="Football Video Analysis Setup", font=("Arial", 16, "bold")).grid(row=0, column=0, columnspan=2, pady=(0, 20))

        # Video Selection
        tk.Label(main_frame, text="Input Video:", font=("Arial", 10, "bold")).grid(row=1, column=0, sticky="w")
        tk.Button(main_frame, text="Select Video File", command=self.select_video).grid(row=2, column=0, columnspan=2, pady=5)
        tk.Label(main_frame, textvariable=self.video_path, wraplength=350).grid(row=3, column=0, columnspan=2, pady=5)

        # Output Filename
        tk.Label(main_frame, text="Output Filename:").grid(row=4, column=0, sticky="w")
        tk.Entry(main_frame, textvariable=self.output_path, width=30).grid(row=4, column=1, pady=10)

        # Batch Size
        tk.Label(main_frame, text="Batch Size:").grid(row=5, column=0, sticky="w")
        tk.Entry(main_frame, textvariable=self.batch_size, width=5).grid(row=5, column=1, pady=10)

        # Process Button
        tk.Button(main_frame, text="Process Video", command=self.process_video, bg="green", fg="white", font=("Arial", 12, "bold"), width=15, height=2).grid(row=6, column=0, columnspan=2, pady=20)

    def select_video(self):
        video_path = filedialog.askopenfilename(
            initialdir="input_videos",
            title="Select Video File",
            filetypes=(("Video files", "*.mp4 *.avi *.mov"), ("All files", "*.*"))
        )
        if video_path:
            self.video_path.set(video_path)

    def process_video(self):
        # Validation
        if not self.video_path.get():
            messagebox.showerror("Error", "Please select a video file")
            return

        output_path = self.output_path.get()
        if not output_path:
            messagebox.showerror("Error", "Please enter an output filename")
            return
        if not output_path.endswith('.mp4'):
            output_path += '.mp4'
        output_path = os.path.join('output_videos', output_path)

        # 1. Load the object detection model
        obj_tracker = ObjectTracker(
            model_path='models/weights/object-detection.pt',
            conf=.5,
            ball_conf=.05
        )

        # 2. Load the keypoints detection model
        kp_tracker = KeypointsTracker(
            model_path='models/weights/keypoints-detection.pt',
            conf=.3,
            kp_conf=.7
        )

        # 3. Assign clubs with hardcoded colors
        club1 = Club('Club1', (232, 247, 248), (6, 25, 21))
        club2 = Club('Club2', (172, 251, 145), (239, 156, 132))
        club_assigner = ClubAssigner(club1, club2)
        ball_player_assigner = BallToPlayerAssigner(club1, club2)

        # 4. Define top-down keypoints
        top_down_keypoints = np.array([
            [0, 0], [0, 57], [0, 122], [0, 229], [0, 293], [0, 351],
            [32, 122], [32, 229],
            [64, 176],
            [96, 57], [96, 122], [96, 229], [96, 293],
            [263, 0], [263, 122], [263, 229], [263, 351],
            [431, 57], [431, 122], [431, 229], [431, 293],
            [463, 176],
            [495, 122], [495, 229],
            [527, 0], [527, 57], [527, 122], [527, 229], [527, 293], [527, 351],
            [210, 176], [317, 176]
        ])

        # 5. Initialize the video processor
        processor = FootballVideoProcessor(
            obj_tracker,
            kp_tracker,
            club_assigner,
            ball_player_assigner,
            top_down_keypoints,
            field_img_path='input_videos/field_2d_v2.png',
            save_tracks_dir='output_videos',
            draw_frame_num=True
        )

        # 6. Process the video
        messagebox.showinfo("Processing", f"Processing video: {self.video_path.get()}\nOutput will be saved to: {output_path}\nThis may take a while.")
        process_video(
            processor,
            video_source=self.video_path.get(),
            output_video=output_path,
            batch_size=self.batch_size.get()
        )
        messagebox.showinfo("Complete", "Processing complete!")

def main():
    root = tk.Tk()
    app = FootballAnalysisGUI(root)
    root.mainloop()

if __name__ == '__main__':
    main()